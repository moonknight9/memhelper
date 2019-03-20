package de.eschwank.ui.encoders

import java.time.LocalDate
import java.time.format.DateTimeFormatter

import com.vaadin.flow.templatemodel.ModelEncoder

/**
 * Converts between DateTime-objects and their String-representations
 *
 */

class LocalDateToStringEncoder : ModelEncoder<LocalDate, String> {

    override fun decode(presentationValue: String): LocalDate {
        return LocalDate.parse(presentationValue, DATE_FORMAT)
    }

    override fun encode(modelValue: LocalDate?): String? {
        return modelValue?.format(DATE_FORMAT)
    }

    companion object {

        val DATE_FORMAT = DateTimeFormatter
                .ofPattern("MM/dd/yyyy")
    }

}
