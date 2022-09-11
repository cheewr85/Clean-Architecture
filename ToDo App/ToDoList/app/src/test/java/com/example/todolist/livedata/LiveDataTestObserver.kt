package com.example.todolist.livedata

import androidx.lifecycle.Observer

// LiveData를 바로 사용할 수 없어서 동작을 비슷하게 따라한 코드(테스트를 위한 유틸리티 클래스)
// 옵저버를 그대로 구현해 값이 바뀔 때마다 상태를 확인함
class LiveDataTestObserver<T> : Observer<T> {

    private val values: MutableList<T> = mutableListOf()

    override fun onChanged(t: T) {
        values.add(t)
    }

    // 기대값과 바뀌게 된 특정 인스턴스에 순서가 같은지 확인을 함
    // 값 변경을 확인해서 결과를 리턴하게 함, 문제없으면 성공하게 뜸
    // 다 테스트에 대해서 처리하는 것
    fun assertValueSequence(sequence: List<T>): LiveDataTestObserver<T> {
        var i = 0
        val actualIterator = values.iterator()
        val expectedIterator = sequence.iterator()

        var actualNext: Boolean
        var expectedNext: Boolean

        while (true) {
            actualNext = actualIterator.hasNext()
            expectedNext = expectedIterator.hasNext()

            if (!actualNext || !expectedNext) break

            val actual: T = actualIterator.next()
            val expected: T = expectedIterator.next()

            if (actual != expected) {
                throw AssertionError("actual: ${actual}, expected: ${expected}, index $i")
            }

            i++
        }

        if (actualNext) {
            throw AssertionError("More values received than expected ($i)")
        }
        if (expectedNext) {
            throw AssertionError("Fewer values received than expected ($i)")
        }

        return this
    }
}