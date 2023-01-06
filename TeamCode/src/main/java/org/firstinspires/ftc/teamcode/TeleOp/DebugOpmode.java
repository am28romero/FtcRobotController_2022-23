package org.firstinspires.ftc.teamcode.TeleOp;

//import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
//import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;

@TeleOp(name="Debug", group="Iterative Opmode")
public class DebugOpmode extends OpMode
{
	private final ElapsedTime runtime = new ElapsedTime();
	private final DcMotor[] driveMotors = new DcMotor[4];
	private DcMotor linearSlide;
	private Servo cupServo;
	private int slideLocation;

	@Override
	public void init() {
		telemetry.addData("Status", "Initialized");

		driveMotors[0] = hardwareMap.get(DcMotor.class, "frontleft");
		driveMotors[1] = hardwareMap.get(DcMotor.class, "frontright");
		driveMotors[2] = hardwareMap.get(DcMotor.class, "backleft");
		driveMotors[3] = hardwareMap.get(DcMotor.class, "backright");
		linearSlide = hardwareMap.get(DcMotor.class, "linearslide");
		cupServo = hardwareMap.get(Servo.class, "cupservo");

		driveMotors[0].setDirection(DcMotor.Direction.REVERSE);
		driveMotors[1].setDirection(DcMotor.Direction.REVERSE);
		driveMotors[2].setDirection(DcMotor.Direction.REVERSE);
		driveMotors[3].setDirection(DcMotor.Direction.REVERSE);

		for (DcMotor driveMotor:driveMotors) {
			driveMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
			driveMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
		}

		linearSlide.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
		linearSlide.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

		telemetry.addData("Status", "Initialized");
		telemetry.addData("Version", "v1.12 Debug");
		telemetry.addData("Cup Servo Debug", cupServo.getConnectionInfo());
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
		//leftPower      = Range.clip(drivefb + turn, -1.0, 1.0);
		//rightPower     = Range.clip(drivefb - turn, -1.0, 1.0);

		driveMotors[0].setPower(Range.clip(drivefb + turn + drivelr, -1.0, 1.0));  //fl
		driveMotors[1].setPower(Range.clip(drivefb - turn - drivelr, -1.0, 1.0));  //fr
		driveMotors[2].setPower(Range.clip(drivefb + turn - drivelr, -1.0, 1.0));  //bl
		driveMotors[3].setPower(Range.clip(drivefb - turn + drivelr, -1.0, 1.0));  //br

		if (gamepad2.a && cupServo.getPosition()>= 0.5) cupServo.setPosition(0.40);  //
		else if (gamepad2.b && cupServo.getPosition() < 0.5) cupServo.setPosition(0.92); //

		if (gamepad2.left_trigger > 0.01) {
			linearSlide.setPower(1*gamepad2.left_trigger);
			slideLocation = linearSlide.getCurrentPosition();
		}
		else if (gamepad2.right_trigger > 0.01) {
			linearSlide.setPower(-1*gamepad2.right_trigger);
			slideLocation = linearSlide.getCurrentPosition();
		}
		else {
			linearSlide.setPower(0.0);
			linearSlide.setTargetPosition(slideLocation);
		}


		telemetry.addData("Status", "Run Time: " + runtime);
		telemetry.addData("Servos", "Cup (%.2f)", cupServo.getPosition());
	}

	@Override
	public void stop() {
		for (DcMotor driveMotor:driveMotors) driveMotor.setPower(0.0);
	}
}
