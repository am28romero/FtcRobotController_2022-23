package org.firstinspires.ftc.teamcode.TeleOp;

//import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;

@TeleOp(name="LTS", group="Iterative Opmode")
public class LTSOpmodeIterative extends OpMode
{
	private final ElapsedTime runtime = new ElapsedTime();
	private final DcMotor[] driveMotors = new DcMotor[4];

	@Override
	public void init() {
		telemetry.addData("Status", "Initialized");

		driveMotors[0] = hardwareMap.get(DcMotor.class, "frontleft");
		driveMotors[1] = hardwareMap.get(DcMotor.class, "frontright");
		driveMotors[2] = hardwareMap.get(DcMotor.class, "backleft");
		driveMotors[3] = hardwareMap.get(DcMotor.class, "backright");

		driveMotors[0].setDirection(DcMotor.Direction.REVERSE);
		driveMotors[1].setDirection(DcMotor.Direction.REVERSE);
		driveMotors[2].setDirection(DcMotor.Direction.REVERSE);
		driveMotors[3].setDirection(DcMotor.Direction.REVERSE);

		for (DcMotor driveMotor:driveMotors) {
			driveMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
			driveMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
		}

		telemetry.addData("Status", "Initialized");
		telemetry.addData("Version", "v1.2 LTS");
	}

	@Override
	public void init_loop() {}

	@Override
	public void start() {
		runtime.reset();
	}

	@Override
	public void loop() {
		double drivefb = -gamepad1.left_stick_y;
		double drivelr = gamepad1.left_stick_x;
		double turn    =  gamepad1.right_stick_x;

		driveMotors[0].setPower(Range.clip(drivefb + turn + drivelr, -1.0, 1.0));  //fl
		driveMotors[1].setPower(Range.clip(drivefb - turn - drivelr, -1.0, 1.0));  //fr
		driveMotors[2].setPower(Range.clip(drivefb + turn - drivelr, -1.0, 1.0));  //bl
		driveMotors[3].setPower(Range.clip(drivefb - turn + drivelr, -1.0, 1.0));  //br

		telemetry.addData("Status", "Run Time: " + runtime);
		//telemetry.addData("Motors", "left (%.2f), right (%.2f)", leftPower, rightPower);
	}

	@Override
	public void stop() {
		for (DcMotor driveMotor:driveMotors) driveMotor.setPower(0.0);
	}
}
