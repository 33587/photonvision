package frc.robot.Subsystems;

import org.photonvision.PhotonCamera;
import org.photonvision.targeting.PhotonPipelineResult;
import org.photonvision.targeting.PhotonTrackedTarget;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import java.util.List;

public class PhotonVisionSubsystem extends SubsystemBase {
    private final PhotonCamera camera;

    public PhotonVisionSubsystem(String cameraName) {
        camera = new PhotonCamera(cameraName); 
    }

    public boolean hasTarget() {
        return camera.getLatestResult().hasTargets();
    }

    public double getYaw() {
        PhotonPipelineResult result = camera.getLatestResult();
        return result.hasTargets() ? result.getBestTarget().getYaw() : 0.0;
    }

    public List<PhotonTrackedTarget> getAllTargets() {
        PhotonPipelineResult result = camera.getLatestResult();
        return result.hasTargets() ? result.getTargets() : List.of();
    }

    public PhotonTrackedTarget getBestTarget() {
        PhotonPipelineResult result = camera.getLatestResult();
        return result.hasTargets() ? result.getBestTarget() : null;
    }

    public void setPipeline(int pipelineIndex) {
        camera.setPipelineIndex(pipelineIndex); // Switch pipelines dynamically
        camera.setDriverMode(true);
    }

    @Override
    public void periodic() {
        // Get all detected targets
        List<PhotonTrackedTarget> targets = getAllTargets();

        // Log the number of detected targets to SmartDashboard
        SmartDashboard.putNumber("PhotonVision/Number of Targets", targets.size());

        // Update best target details
        PhotonTrackedTarget bestTarget = getBestTarget();
        if (bestTarget != null) {
            SmartDashboard.putNumber("PhotonVision/Best Target Yaw", bestTarget.getYaw());
            SmartDashboard.putNumber("PhotonVision/Best Target Pitch", bestTarget.getPitch());
            SmartDashboard.putNumber("PhotonVision/Best Target Area", bestTarget.getArea());
        } else {
            SmartDashboard.putString("PhotonVision/Best Target", "No targets");
        }

        // Log whether any targets are visible
        SmartDashboard.putBoolean("PhotonVision/Has Target", hasTarget());
    }
}
