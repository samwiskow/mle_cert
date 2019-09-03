package mle.cert.assignment.run;

import java.nio.file.Paths;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

import com.workfusion.vds.sdk.run.ModelRunner;
import com.workfusion.vds.sdk.run.config.LocalExecutionConfiguration;

import mle.cert.assignment.model.AssignmentModel;

/**
 *  Runner class for local model execution.
 *  Could be used for model tuning and post-processing development.
 *  Paths to input documents, trained model and output folders are required for the lauch.
 */
public class ModelExecutionRunner {

    public static void main(String[] args) throws Exception {
        System.setProperty("WORKFLOW_LOG_FOLDER", "./logs/");

        //TODO put correct values for the paths
        Path trainedModelPath = Paths.get("output/training/output/model");
        Path inputDirPath = Paths.get("validation");
        Path outputDirPath = Paths.get("extraction");

        //TODO add parameters if needed.
        Map<String, Object> parameters = new HashMap<>();

        LocalExecutionConfiguration configuration = LocalExecutionConfiguration.builder()
                .inputDir(inputDirPath)
                .outputDir(outputDirPath)
                .trainedModelDir(trainedModelPath)
                .parameters(parameters)
                .build();

        ModelRunner.run(AssignmentModel.class, configuration);
    }
}
