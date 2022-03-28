package com.example.acl.frontend.components.inputs

import com.example.acl.frontend.models.FieldValidator
import com.example.acl.frontend.models.FileDefinition
import com.example.filehandler.domains.fileuploads.models.entities.UploadProperties
import com.example.filehandler.domains.fileuploads.services.FileUploadService
import com.vaadin.flow.component.Component
import com.vaadin.flow.component.html.Image
import com.vaadin.flow.component.html.Label
import com.vaadin.flow.component.orderedlayout.VerticalLayout
import com.vaadin.flow.component.upload.SucceededEvent
import com.vaadin.flow.component.upload.Upload
import com.vaadin.flow.component.upload.receivers.MultiFileMemoryBuffer
import java.io.InputStream

class MultiUploadInput(
	private val uploadService: FileUploadService,
	private val fileDefinition: FileDefinition,
	private val uploadListener: FileUploadListener?,
	override var fieldValidator: FieldValidator<List<String>>?
) : Upload(MultiFileMemoryBuffer()), AbstractInput<List<String>> {

	private var label: String = ""
	private var urls: MutableList<String> = mutableListOf()

	constructor(
		id: String,
		label: String,
		uploadService: FileUploadService,
		fileDefinition: FileDefinition,
		uploadListener: FileUploadListener?,
		fieldValidator: FieldValidator<List<String>>?
	) : this(uploadService, fileDefinition, uploadListener, fieldValidator) {

		this.setId(id)
		this.label = label
		this.dropLabel = Label(label)
		this.buildComponent()

	}

	fun setDefaultImages(urls: MutableList<String>, alt: String) {
		this.urls = urls
		val layout = VerticalLayout()
		layout.add(Label(this.label))

		this.urls.forEach { url ->
			val image = Image(url, alt)
			image.addClickListener {
				this.urls.remove(url)
				this.setDefaultImages(this.urls, alt)
			}
			layout.add(image)
		}

		this.dropLabel = layout
	}

	fun buildComponent() {

		this.addSucceededListener { event: SucceededEvent ->
			// Determine which file was uploaded
			val fileName = event.fileName

			// Get input stream specifically for the finished file
			val fileData: InputStream = (this.receiver as MultiFileMemoryBuffer).getInputStream(fileName)
			val properties: UploadProperties = this.uploadService.uploadFile(
				fileData.readBytes(), this.fileDefinition.type, fileDefinition.namespace, fileDefinition.uniqueProperty
			)
			val contentLength = event.contentLength
			val mimeType = event.mimeType
			uploadListener?.onFileUploaded(properties, contentLength, mimeType)
			this.urls.add(properties.fileUrl)
		}

	}


	interface FileUploadListener {
		fun onFileUploaded(properties: UploadProperties, contentLength: Long, mimeType: String)
	}

	override fun setVal(value: List<String>) {

	}

	override fun getVal(): List<String>? {
		return this.urls
	}

	override fun clearVal() {
		this.urls.clear()
	}

	override fun getComponent(): Component {
		return this
	}

	override fun getValidator(): FieldValidator<List<String>>? {
		return this.fieldValidator
	}

	override fun setMessage(error: Boolean, message: String?) {
		this.resolveClass(this, error)
		if (message != null) this.dropLabel = Label(message)
	}

}