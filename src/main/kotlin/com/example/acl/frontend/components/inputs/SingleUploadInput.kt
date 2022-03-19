package com.example.acl.frontend.components.inputs

import com.example.acl.frontend.models.FieldValidator
import com.example.acl.frontend.models.FileDefinition
import com.example.cms.domains.fileuploads.models.entities.UploadProperties
import com.example.cms.domains.fileuploads.services.FileUploadService
import com.vaadin.flow.component.Component
import com.vaadin.flow.component.html.Image
import com.vaadin.flow.component.html.Label
import com.vaadin.flow.component.orderedlayout.VerticalLayout
import com.vaadin.flow.component.upload.SucceededEvent
import com.vaadin.flow.component.upload.Upload
import com.vaadin.flow.component.upload.receivers.FileBuffer
import java.io.InputStream

class SingleUploadInput(
	private val uploadService: FileUploadService,
	private val fileDefinition: FileDefinition,
	private val uploadListener: FileUploadListener?,
	override var fieldValidator: FieldValidator<String>?
) : Upload(FileBuffer()), AbstractInput<String> {

	private var label: String = ""
	private var url: String? = null

	constructor(
		id: String,
		label: String,
		uploadService: FileUploadService,
		fileDefinition: FileDefinition,
		uploadListener: FileUploadListener?,
		fieldValidator: FieldValidator<String>?
	) : this(uploadService, fileDefinition, uploadListener, fieldValidator) {

		this.setId(id)
		this.label = label
		this.dropLabel = Label(label)
		this.buildComponent()

	}

	fun setDefaultImage(url: String?, alt: String) {
		this.url = url
		if (url == null) return

		val layout = VerticalLayout()
		val image = Image(url, alt)
		layout.add(Label(this.label))
		layout.add(image)
		this.dropLabel = layout
	}

	fun buildComponent() {

		this.addSucceededListener { event: SucceededEvent ->
			// Determine which file was uploaded
			val fileName = event.fileName

			// Get input stream specifically for the finished file
			val fileData: InputStream = (this.receiver as FileBuffer).inputStream
			val properties: UploadProperties = this.uploadService.uploadFile(
				fileData.readBytes(), this.fileDefinition.type, fileDefinition.namespace, fileDefinition.uniqueProperty
			)
			val contentLength = event.contentLength
			val mimeType = event.mimeType
			uploadListener?.onFileUploaded(properties, contentLength, mimeType)
			this.url = properties.fileUrl
		}

	}


	interface FileUploadListener {
		fun onFileUploaded(properties: UploadProperties, contentLength: Long, mimeType: String)
	}

	override fun setVal(value: String) {

	}

	override fun getVal(): String? {
		return this.url
	}

	override fun clearVal() {
		this.url = null
	}

	override fun getComponent(): Component {
		return this
	}

	override fun getValidator(): FieldValidator<String>? {
		return this.fieldValidator
	}

	override fun setMessage(error: Boolean, message: String?) {
		this.resolveClass(this, error)
		if (message != null) this.dropLabel = Label(message)
	}

}