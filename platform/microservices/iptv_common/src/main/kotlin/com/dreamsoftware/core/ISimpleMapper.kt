package com.dreamsoftware.core

interface ISimpleMapper<INPUT, OUTPUT> {
    fun map(input: INPUT): OUTPUT
    fun mapList(input: Iterable<INPUT>): Iterable<OUTPUT>
}