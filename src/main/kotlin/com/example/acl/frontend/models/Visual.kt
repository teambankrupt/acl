package com.example.acl.frontend.models

import com.vaadin.flow.data.renderer.Renderer

data class Visual<T>(var label: String, var renderer: Renderer<T>?)
