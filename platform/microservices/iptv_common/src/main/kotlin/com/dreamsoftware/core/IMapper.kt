package com.dreamsoftware.core

interface IMapper<INPUT, OUTPUT> {
    fun map(input: INPUT): OUTPUT
    fun mapList(input: Iterable<INPUT>): Iterable<OUTPUT>
    fun mapReverse(output: OUTPUT): INPUT { TODO( "Not implemented yet")}
    fun mapReverseList(output: Iterable<OUTPUT>): Iterable<INPUT> { TODO( "Not implemented yet") }
}