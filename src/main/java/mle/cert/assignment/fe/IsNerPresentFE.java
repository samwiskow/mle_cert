/*
 * Copyright (C) WorkFusion 2018. All rights reserved.
 */
package mle.cert.assignment.fe;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.workfusion.vds.sdk.api.nlp.fe.Feature;
import com.workfusion.vds.sdk.api.nlp.fe.FeatureExtractor;
import com.workfusion.vds.sdk.api.nlp.fe.annotation.FeatureName;
import com.workfusion.vds.sdk.api.nlp.model.Document;
import com.workfusion.vds.sdk.api.nlp.model.Element;
import com.workfusion.vds.sdk.api.nlp.model.NamedEntity;

/**
 * Assignment 1
 */
@FeatureName(IsNerPresentFE.FEATURE_NAME)
public class IsNerPresentFE<T extends Element> implements FeatureExtractor<T> {

	public static final String FEATURE_NAME = "is_ner_present";
    private final String nerType;
 
    public IsNerPresentFE(String mentionType) {
        this.nerType = mentionType;
    }
 
    /**
     * Determines if focus annotation is covering by NER
     * @param document {@link Document} the Document containing the focus
     * @param element {@link Element} the current focus
     * @return "1" if focus annotation is covering by NER, empty list otherwise
     */
    @Override
    public Collection<Feature> extract(Document document, T element) {
        List<Feature> result = new ArrayList<>();
 
        List<NamedEntity> namedEntity = document.findCovering(NamedEntity.class, element);
        for (NamedEntity ner : namedEntity) {
            if (ner.getType().equalsIgnoreCase(nerType)) {
                result.add(new Feature(FEATURE_NAME, 1));
            }
        }
        
        //result.add(new Feature(FEATURE_NAME,1));
 
        return result;
    }

}