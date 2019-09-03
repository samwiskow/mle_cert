package mle.cert.assignment.model;

import com.workfusion.automl.hypermodel.ie.generic.IeGenericSe20Hypermodel;
import com.workfusion.vds.sdk.api.hypermodel.ModelType;
import com.workfusion.vds.sdk.api.hypermodel.annotation.HypermodelConfiguration;
import com.workfusion.vds.sdk.api.hypermodel.annotation.ModelDescription;

import mle.cert.assignment.config.AssignmentModelConfiguration;

/**
 * The model class. Define here your model details like code, version etc.
 */
@ModelDescription(
        code = "assignment",
        title = "Assignment Cert",
        description = "MLE certification attempt",
        version = "0.0.1-SNAPSHOT",
        type = ModelType.IE
)
@HypermodelConfiguration(AssignmentModelConfiguration.class)
public class AssignmentModel extends IeGenericSe20Hypermodel {}