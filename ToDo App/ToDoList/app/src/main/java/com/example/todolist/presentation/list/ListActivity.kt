package com.example.todolist.presentation.list

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.core.view.isGone
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.todolist.R
import com.example.todolist.databinding.ActivityListBinding
import com.example.todolist.presentation.BaseActivity
import com.example.todolist.presentation.detail.DetailActivity
import com.example.todolist.presentation.detail.DetailMode
import com.example.todolist.presentation.view.ToDoAdapter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import org.koin.android.ext.android.inject
import kotlin.coroutines.CoroutineContext

// 외부 모듈에서 쓸 일이 없기 때문에 액티비티여도 internal로 함
internal class ListActivity : BaseActivity<ListViewModel>(), CoroutineScope {

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + Job()

    private lateinit var binding: ActivityListBinding

    // 현재 버전에선 inject를 통해서 의존성 주입함
    override val viewModel: ListViewModel by inject()

    private val adapter = ToDoAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityListBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    private fun initViews(binding: ActivityListBinding) = with(binding) {
        recyclerView.layoutManager = LinearLayoutManager(this@ListActivity, LinearLayoutManager.VERTICAL, false)
        recyclerView.adapter = adapter

        refreshLayout.setOnRefreshListener {
            viewModel.fetchData()
        }

        addToDoButton.setOnClickListener {
            startActivityForResult(
                DetailActivity.getIntent(this@ListActivity, DetailMode.WRITE),
                DetailActivity.FETCH_REQUEST_CODE
            )
        }
    }

    override fun observeData() {
        viewModel.todoListLiveData.observe(this) {
            // 상태값에 따라 LiveData 초기화를 다르게 해 줌
            when (it) {
                is ToDoListState.UnInitialized -> {
                    initViews(binding)
                }
                is ToDoListState.Loading -> {
                    handleLoadingState()
                }
                is ToDoListState.Success -> {
                    handleSuccessState(it)
                }
                is ToDoListState.Error -> {
                    handleErrorState()
                }
            }
        }
    }

    // UI에 대한 변화를 추가해서 처리함
    private fun handleErrorState() {
        Toast.makeText(this, "에러가 발생했습니다.",Toast.LENGTH_SHORT).show()
    }

    private fun handleLoadingState() = with(binding) {
        refreshLayout.isRefreshing = true
    }

    private fun handleSuccessState(state: ToDoListState.Success) = with(binding) {
        refreshLayout.isEnabled = state.toDoList.isNotEmpty()
        refreshLayout.isRefreshing = false

        if (state.toDoList.isEmpty()) {
            emptyResultTextView.isGone = false
            recyclerView.isGone = true
        } else {
            emptyResultTextView.isGone = true
            recyclerView.isGone = false
            adapter.setToDoList(
                state.toDoList,
                toDoItemClickListener = {
                    startActivityForResult(
                        DetailActivity.getIntent(this@ListActivity, it.id, DetailMode.DETAIL),
                        DetailActivity.FETCH_REQUEST_CODE
                    )
                }, toDoCheckListener = {
                    viewModel.updateEntity(it)
                }
            )
        }
    }

    // Detail에서 처리한 결과를 반영하게 fetch함
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == DetailActivity.FETCH_REQUEST_CODE && resultCode == RESULT_OK) {
            viewModel.fetchData()
        }
    }

    // 아이템 선택시 상호작용 처리를 함
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_delete_all -> {
                viewModel.deleteAll()
                true
            }
            else -> {
                super.onOptionsItemSelected(item)
            }
        }

    }

    // 메뉴 생성을 함
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.list_menu, menu)
        return true
    }
}