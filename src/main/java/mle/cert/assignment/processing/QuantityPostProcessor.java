package mle.cert.assignment.processing;

import com.workfusion.vds.sdk.api.nlp.model.Field;
import com.workfusion.vds.sdk.api.nlp.model.IeDocument;
import com.workfusion.vds.sdk.api.nlp.processing.Processor;
import com.workfusion.vds.sdk.nlp.component.processing.normalization.OcrAmountNormalizer;

import java.util.Collection;

public class QuantityPostProcessor implements Processor<IeDocument> {

    private static final String REGEX_TOTAL_AMOUNT_WRONG_CHARS  = "[,($]";

    @Override
    public void process(IeDocument document) {

        Collection<Field> fields = document.findFields("quantity");

        for(Field amountField : fields)
        {
            String value = amountField.getText() + ".00";
            amountField.setValue(value);

        }

    }

}
