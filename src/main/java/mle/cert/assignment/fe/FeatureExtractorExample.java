package mle.cert.assignment.fe;

import com.workfusion.vds.sdk.api.nlp.annotation.OnDocumentComplete;
import com.workfusion.vds.sdk.api.nlp.annotation.OnDocumentStart;
import com.workfusion.vds.sdk.api.nlp.fe.Feature;
import com.workfusion.vds.sdk.api.nlp.fe.FeatureExtractor;
import com.workfusion.vds.sdk.api.nlp.model.Document;
import com.workfusion.vds.sdk.api.nlp.model.Element;

import java.util.Collection;

import static java.util.Collections.emptyList;

public class FeatureExtractorExample<T extends Element> implements FeatureExtractor<T> {
    
    @OnDocumentStart
    public void onStart(Document document, Class<T> focusClass) {
        //TODO add on document start logic here
    }

    @Override
    public Collection<Feature> extract(Document document, T element) {
        //TODO add feature extraction logic here
        return emptyList();
    }
    
    @OnDocumentComplete
    public void onComplete() {
      //TODO add on document complete logic here
    }    
    
}
