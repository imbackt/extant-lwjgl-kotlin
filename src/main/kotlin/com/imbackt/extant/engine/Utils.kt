package com.imbackt.extant.engine

fun loadResource(filename: String) = object {}.javaClass.classLoader.getResource(filename).readText()