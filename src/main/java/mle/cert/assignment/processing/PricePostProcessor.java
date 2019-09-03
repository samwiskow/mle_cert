package mle.cert.assignment.processing;

import com.workfusion.vds.sdk.api.nlp.model.Field;
import com.workfusion.vds.sdk.api.nlp.model.IeDocument;
import com.workfusion.vds.sdk.api.nlp.processing.Processor;
import com.workfusion.vds.sdk.nlp.component.processing.normalization.OcrAmountNormalizer;

import java.util.Collection;

public class PricePostProcessor  implements Processor<IeDocument> {

    private static final String REGEX_TOTAL_AMOUNT_WRONG_CHARS  = "[,($]";

    @Override
    public void process(IeDocument document) {

        Collection<Field> fields = document.findFields("price");

        for(Field amountField : fields)
        {
            String value = amountField.getText();
            String correctedValue = value
                    /*.replaceAll("G|b", "6")
                    .replaceAll("B", "8")
                    .replaceAll("I|l|i|\\|", "1")
                    .replaceAll("O", "0")*/;
            OcrAmountNormalizer amountNormalizer = new OcrAmountNormalizer();
            correctedValue = amountNormalizer.normalize(correctedValue);
            amountField.setValue(correctedValue);

        }

    }

}
