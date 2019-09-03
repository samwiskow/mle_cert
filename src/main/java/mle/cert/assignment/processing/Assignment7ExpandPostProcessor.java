/*
 * Copyright (C) WorkFusion 2018. All rights reserved.
 */
package mle.cert.assignment.processing;

import java.util.Collection;
import java.util.List;

import com.workfusion.vds.sdk.api.nlp.model.Field;
import com.workfusion.vds.sdk.api.nlp.model.IeDocument;
import com.workfusion.vds.sdk.api.nlp.model.Line;
import com.workfusion.vds.sdk.api.nlp.processing.Processor;

/**
 * Assignment 7
 */
public class Assignment7ExpandPostProcessor implements Processor<IeDocument> {

    /**
     * Name of {@link Field} representing a product.
     */
    public static final String FIELD_NAME = "product";

    @Override
    public void process(IeDocument document) {

    	Collection<Field> fields = document.findFields(FIELD_NAME);
    	if (fields != null){
			for(Field f : fields) {
				List<Line> lines = document.findCovering(Line.class, f);
				document.remove(f);
				document.add(Field.descriptor().setName("product").setValue(lines.get(0).getText()).setBegin(lines.get(0).getBegin())
						.setEnd(lines.get(0).getEnd()).setScore(f.getScore()));
			}
		}

    }

}