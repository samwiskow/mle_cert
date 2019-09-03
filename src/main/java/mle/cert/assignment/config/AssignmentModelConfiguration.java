package mle.cert.assignment.config;

import java.util.*;
import java.util.concurrent.TimeUnit;

import com.workfusion.vds.nlp.hypermodel.ie.generic.config.GenericIeHypermodelConfiguration;
import com.workfusion.vds.sdk.api.hpo.Dimensions;
import com.workfusion.vds.sdk.api.hpo.HpoConfiguration;
import com.workfusion.vds.sdk.api.hpo.ParameterSpace;
import com.workfusion.vds.sdk.api.hypermodel.ConfigurationContext;
import com.workfusion.vds.sdk.api.hypermodel.annotation.Filter;
import com.workfusion.vds.sdk.api.hypermodel.annotation.Import;
import com.workfusion.vds.sdk.api.hypermodel.annotation.ModelConfiguration;
import com.workfusion.vds.sdk.api.hypermodel.annotation.Named;
import com.workfusion.vds.sdk.api.nlp.annotator.Annotator;
import com.workfusion.vds.sdk.api.nlp.configuration.IeConfigurationContext;
import com.workfusion.vds.sdk.api.nlp.fe.FeatureExtractor;
import com.workfusion.vds.sdk.api.nlp.model.*;
import com.workfusion.vds.sdk.api.nlp.processing.Processor;
import com.workfusion.vds.sdk.nlp.component.annotator.EntityBoundaryAnnotator;
import com.workfusion.vds.sdk.nlp.component.annotator.ner.AhoCorasickDictionaryNerAnnotator;
import com.workfusion.vds.sdk.nlp.component.annotator.ner.BaseRegexNerAnnotator;
import com.workfusion.vds.sdk.nlp.component.annotator.tokenizer.MatcherTokenAnnotator;
import com.workfusion.vds.sdk.nlp.component.dictionary.CsvDictionaryKeywordProvider;
import com.workfusion.vds.sdk.nlp.component.processing.grouping.RowBasedGroupingProcessor;
import mle.cert.assignment.fe.*;
import mle.cert.assignment.processing.*;

/**
 * The model configuration class.
 * Here you can configure set of Feature Extractors, Annotators and Post-Processors.
 * Also you can import configuration with set of predefined components or your own configuration
 * */
@ModelConfiguration
@Import(configurations = {
        @Import.Configuration(value = GenericIeHypermodelConfiguration.class)

        })
public class AssignmentModelConfiguration {

    /**
     * Name of {@link Field} representing a product.
     */
    public final static String FIELD_PRODUCT = "product";

    /**
     * Regex pattern to use for matching {@link Token} elements.
     */
    private final static String TOKEN_REGEX = "[\\w@.,$%’-]+";

    /**
     * Name of {@link Field} representing an invoice number.
     */
    public final static String FIELD_INVOICE_NUMBER = "invoice_number";

    /**
     * Regex pattern to match an invoice number.
     */
    private final static String INVOICE_NUMBER_REGEX = "\\d{10}";

    /**
     * Name of {@link Field} representing an email.
     */
    public final static String FIELD_EMAIL = "email";

    /**
     * Regex pattern to match an email.
     */
    private final static String EMAIL_REGEX = "[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,4}";

    /**
     * Name of {@link Field} representing a price.
     */
    public final static String FIELD_PRICE = "price";

    /**
     * Regex pattern to match a price.
     */
    private final static String PRICE_REGEX = "(\\$[0-9]{1,3}[.][0-9]{2})";

    /**
     * Name of {@link Field} representing a client name.
     */
    public final static String FIELD_SUPPLIER_NAME = "supplier_name";

    /**
     * Regex pattern to match a client name.
     */
    private static final String CLIENT_NAME_REGEX = "([A-Z][a-z ]{3,11}){3,4}";

    @Named("annotators")
    public List<Annotator> annotators(IeConfigurationContext context) {
        List<Annotator> annotators = new ArrayList<>();
        annotators.add(new MatcherTokenAnnotator(TOKEN_REGEX));
        annotators.add(new EntityBoundaryAnnotator());

        switch (context.getField().getCode()) {
            case FIELD_INVOICE_NUMBER: {
                annotators.add(BaseRegexNerAnnotator.getJavaPatternRegexNerAnnotator(FIELD_INVOICE_NUMBER, INVOICE_NUMBER_REGEX));
                break;
            }
            case FIELD_PRICE: {
                annotators.add(BaseRegexNerAnnotator.getJavaPatternRegexNerAnnotator(FIELD_PRICE, PRICE_REGEX));
                break;
            }
            case "total_amount": {
                annotators.add(BaseRegexNerAnnotator.getJavaPatternRegexNerAnnotator("total_amount", PRICE_REGEX));
                break;
            }
            case FIELD_EMAIL: {
                annotators.add(BaseRegexNerAnnotator.getJavaPatternRegexNerAnnotator(FIELD_EMAIL, EMAIL_REGEX));
                break;
            }
            case FIELD_SUPPLIER_NAME: {
                annotators.add(new AhoCorasickDictionaryNerAnnotator(FIELD_SUPPLIER_NAME, new CsvDictionaryKeywordProvider(context.getResource("classpath:dictionary/suppliers.csv"))));
                break;
            }
        }
        return annotators;
    }

    @Named("fe1")
    public List<FeatureExtractor<Element>> getFeatureExtractors(IeConfigurationContext context) {
        List<FeatureExtractor<Element>> featuresExtractors = new ArrayList<>();
        featuresExtractors.add(new RowIndexFE<Element>());

        return featuresExtractors;
    }

    @Named("fe2")
    public List<FeatureExtractor<Element>> getFeatureExtractors2(IeConfigurationContext context) {
        List<FeatureExtractor<Element>> featuresExtractors = new ArrayList<>();
        featuresExtractors.add(new TableNumberFE<Element>());

        return featuresExtractors;
    }

    @Named("fe3")
    public List<FeatureExtractor<Element>> getFeatureExtractors3(IeConfigurationContext context) {
        List<FeatureExtractor<Element>> featuresExtractors = new ArrayList<>();
        featuresExtractors.add(new ColumnIndexFE<Element>());

        return featuresExtractors;
    }
    @Named("fe4")
    public List<FeatureExtractor<Element>> getFeatureExtractors4(IeConfigurationContext context) {
        List<FeatureExtractor<Element>> featuresExtractors = new ArrayList<>();
        featuresExtractors.add(new IsCoveredByNerFE<>(context.getField().getCode()));

        return featuresExtractors;
    }
    @Named("fe5")
    public List<FeatureExtractor<Element>> getFeatureExtractors5(IeConfigurationContext context) {
        List<FeatureExtractor<Element>> featuresExtractors = new ArrayList<>();
        featuresExtractors.add(new IsNerPresentFE<>(context.getField().getCode()));

        return featuresExtractors;
    }

    @Named("fe6")
    public List<FeatureExtractor<Element>> getFeatureExtractors6(IeConfigurationContext context) {
        List<FeatureExtractor<Element>> featuresExtractors = new ArrayList<>();
        Set<String> columnNames = new HashSet<>();
        columnNames.add("price");
        columnNames.add("product");
        columnNames.add("quantity");
        columnNames.add("qty");
        featuresExtractors.add(new MatchFullColumnOrRowWithSpecifiedHeaderFeatureExtractror<>(context.getField().getCode(), columnNames, true));

        return featuresExtractors;
    }

    @Named("fe7")
    public List<FeatureExtractor<Element>> getFeatureExtractors7(IeConfigurationContext context) {
        List<FeatureExtractor<Element>> featuresExtractors = new ArrayList<>();
        featuresExtractors.add(new KeywordsPreviousLineFE<>("Invoice"));

        return featuresExtractors;
    }

    @Named("fe8")
    public List<FeatureExtractor<Element>> getFeatureExtractors8(IeConfigurationContext context) {
        List<FeatureExtractor<Element>> featuresExtractors = new ArrayList<>();
        featuresExtractors.add(new KeywordsPreviousLineFE<>("Total"));

        return featuresExtractors;
    }

    @Named("parameterSpace")
    public ParameterSpace configure(IeConfigurationContext context) {
        ParameterSpace.Builder builder = new ParameterSpace.Builder();

        switch(context.getField().getCode()) {
            case FIELD_INVOICE_NUMBER :{
                builder.add(Dimensions.selectOne("fe3", "fe5", "fe7"));
                break;
            }
            case FIELD_PRODUCT :{
                builder.add(Dimensions.selectOne("fe1","fe2","fe4","fe6"));
                break;
            }
            case FIELD_EMAIL :
            case FIELD_SUPPLIER_NAME: {
                builder.add(Dimensions.required("fe5"));
                break;
            }
            case FIELD_PRICE :
            case "quantity" : {
                builder.add(Dimensions.selectOne("fe4", "fe6"));
                break;
            }
            case "total_amount" : {
                builder.add(Dimensions.selectOne("fe8", "fe5"));
                break;
            }
        }

        return builder.build();
    }

    @Named("basePostProcessors")
    public List<Processor<IeDocument>> getProcessors() {
        List<Processor<IeDocument>> processors = new ArrayList<Processor<IeDocument>>();
        processors.add(new Assignment1DatePostProcessor());
        processors.add(new TotalAmountPostProcessor());
        processors.add(new PricePostProcessor());
        processors.add(new QuantityPostProcessor());
        //processors.add(new Assignment7ExpandPostProcessor());
        processors.add(new RowBasedGroupingProcessor());
        return processors;
    }

    @Named("hpoConfiguration")
    public HpoConfiguration hpoConfiguration(ConfigurationContext context) {
        return new HpoConfiguration.Builder()
                .maxExperimentsWithSameScore(5)
                .build();
    }

}