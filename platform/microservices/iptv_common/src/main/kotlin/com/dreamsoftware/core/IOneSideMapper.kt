package com.dreamsoftware.core

interface IOneSideMapper<INPUT, OUTPUT> {
    fun map(input: INPUT): OUTPUT
    fun mapList(input: Iterable<INPUT>): Iterable<OUTPUT>
}