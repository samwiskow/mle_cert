/*
 * Copyright (C) WorkFusion 2018. All rights reserved.
 */
package mle.cert.assignment.processing;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.Optional;

import com.workfusion.vds.sdk.api.nlp.model.Field;
import com.workfusion.vds.sdk.api.nlp.model.IeDocument;
import com.workfusion.vds.sdk.api.nlp.processing.Processor;
import com.workfusion.vds.sdk.nlp.component.processing.normalization.OcrDateNormalizer;
import com.workfusion.vds.sdk.nlp.component.processing.DataValueNormalizationProcessor;
import com.workfusion.vds.sdk.nlp.component.processing.normalization.TextToDateNormalizer;

/**
 * Assignment 1
 */
public class Assignment1DatePostProcessor implements Processor<IeDocument> {

    /**
     * Name of {@link Field} representing a date.
     */
    public static final String FIELD_NAME = "invoice_date";

    /**
     * A format to which a date needs to be converted in the output.
     */
    private static final String OUTPUT_DATE_FORMAT = "MM/dd/yyyy";

    @Override
    public void process(IeDocument document) {

    	OcrDateNormalizer normalizer = new OcrDateNormalizer(OUTPUT_DATE_FORMAT);
    	
    	Optional<Field> fieldOptional = document.findField("invoice_date");
        if (fieldOptional.isPresent()) {
            Field field = fieldOptional.get();
            String dateString = normalizer.normalize(field.getText());
            //System.out.println("\n" + dateString + "\n");
            field.setValue(dateString);
        }

    }

}