package com.example.acl.frontend.components.deprecated

import com.example.acl.frontend.models.FileDefinition
import com.example.cms.domains.fileuploads.models.entities.UploadProperties
import com.example.cms.domains.fileuploads.services.FileUploadService
import com.vaadin.flow.component.Component
import com.vaadin.flow.component.upload.SucceededEvent
import com.vaadin.flow.component.upload.Upload
import com.vaadin.flow.component.upload.receivers.MultiFileMemoryBuffer
import com.vaadin.flow.function.SerializablePredicate
import java.io.InputStream

class UploadInput<T> : GenericValueInput<T> {
	private var uploadService: FileUploadService
	private var fileDefinition: FileDefinition
	private var uploadListener: FileUploadListener?

	private var multiFile: Boolean = false

	private lateinit var upload: Upload

	constructor(
		fieldName: String,
		label: String,
		validator: SerializablePredicate<in Any>,
		uploadService: FileUploadService,
		fileDefinition: FileDefinition,
		uploadListener: FileUploadListener?,
		multiFile: Boolean
	) : super(
		fieldName, label, validator
	) {
		this.uploadService = uploadService
		this.fileDefinition = fileDefinition
		this.uploadListener = uploadListener
		this.multiFile = multiFile

		this.buildComponent()
	}

	constructor(
		fieldName: String,
		label: String,
		placeholder: String,
		defaultValue: String?,
		validator: SerializablePredicate<in Any>,
		uploadService: FileUploadService,
		fileDefinition: FileDefinition,
		uploadListener: FileUploadListener?,
		multiFile: Boolean
	) : super(fieldName, label, placeholder, defaultValue, validator) {
		this.uploadService = uploadService
		this.fileDefinition = fileDefinition
		this.uploadListener = uploadListener
		this.multiFile = multiFile

		this.buildComponent()
	}

	fun setEnabled(enabled: Boolean){
		this.upload.onEnabledStateChanged(enabled)
	}

	fun buildComponent(){
		val multiFileMemoryBuffer = MultiFileMemoryBuffer()
		val memoryBuffer = MultiFileMemoryBuffer()

		this.upload = Upload(if (this.multiFile) multiFileMemoryBuffer else memoryBuffer)
		this.setEnabled(false)

		this.upload.addSucceededListener { event: SucceededEvent ->
			// Determine which file was uploaded
			val fileName = event.fileName

			// Get input stream specifically for the finished file
			val fileData: InputStream =
				if (this.multiFile) multiFileMemoryBuffer.getInputStream(fileName)
				else memoryBuffer.getInputStream(fileName)
			val properties: UploadProperties = this.uploadService.uploadFile(
				fileData.readBytes(), this.fileDefinition.type, fileDefinition.namespace, fileDefinition.uniqueProperty
			)
			val contentLength = event.contentLength
			val mimeType = event.mimeType
			uploadListener?.onFileUploaded(properties, contentLength, mimeType)
		}

	}

	override fun getComponent(): Component? {
		return this.upload
	}

	interface FileUploadListener {
		fun onFileUploaded(properties: UploadProperties, contentLength: Long, mimeType: String)
	}

}