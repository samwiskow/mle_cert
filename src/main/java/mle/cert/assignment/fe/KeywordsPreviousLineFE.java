/*
  * Copyright (C) WorkFusion 2018. All rights reserved.
 */
package mle.cert.assignment.fe;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.workfusion.vds.nlp.similarity.StringSimilarityUtils;
import com.workfusion.vds.sdk.api.nlp.fe.Feature;
import com.workfusion.vds.sdk.api.nlp.fe.FeatureExtractor;
import com.workfusion.vds.sdk.api.nlp.model.Cell;
import com.workfusion.vds.sdk.api.nlp.model.Document;
import com.workfusion.vds.sdk.api.nlp.model.Element;
import com.workfusion.vds.sdk.api.nlp.model.Line;
import com.workfusion.vds.sdk.api.nlp.model.NamedEntity;

/**
 * Gets similarity of focus annotation to provided keyword
 */
public class KeywordsPreviousLineFE<T extends Element> implements FeatureExtractor<T> {

    /**
     * Name of {@link Feature} the feature extractor produces.
     */
    public static final String FEATURE_NAME = "keywordFeature";

    private String keyword;

    public KeywordsPreviousLineFE(String keyword) {
        this.keyword = keyword.toLowerCase();

    }

    @Override
    public Collection<Feature> extract(Document document, T element) {
        List<Feature> result = new ArrayList<>();
        
 
        List<Line> lines = document.findPrevious(Line.class, element, 30);
        for(Line l : lines) {
        	
         	String lineValue = l.getText().toLowerCase();
         	String[] spr = StringUtils.splitPreserveAllTokens(lineValue);
         	double res =0.0;
         	
         	for (String s: spr) {
 	        	double retValue = StringSimilarityUtils.cosine(keyword, s);
 	        	if (res < retValue) {
 	        		res = retValue;
 	        	}
         	}
         	if (res > 0.0) {
         		result.add(new Feature(FEATURE_NAME, res));
         	}
             
        }

        return result;
    }

}