package com.github.yona168

import java.lang.RuntimeException
import java.nio.file.Path

class InvalidFormatException(badLine: String): RuntimeException(
    "CustomParser encountered an error when parsing at line:\n $badLine"
) {
}