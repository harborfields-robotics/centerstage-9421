package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.Gamepad;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.function.Function;

public class GamePad
{
    private Gamepad gamepad;
    private HashMap<String, Long> timestamps = new HashMap<>();

    public GamePad(Gamepad gamepad)
    {
        this.gamepad = gamepad;
    }
}