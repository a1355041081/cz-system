package cn.geminis.demo.util;

import cn.geminis.demo.eval.DMU;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.HashMap;

public class MathCal {
    private int maxIntegerLength = 0;

    public void setMaxIntegerLength(int maxIntegerLength) {
        this.maxIntegerLength = maxIntegerLength;
    }
    public int getMaxIntegerLength() {
        return maxIntegerLength;
    }

    public int getMaxLength(double[] nums) {
        double max = -Double.MAX_VALUE;
        for (int i = 0; i<nums.length; i++) {
            if (nums[i] > max) max = nums[i];
        }
        DecimalFormat decimalFormat = new DecimalFormat("#0.00");
        decimalFormat.setRoundingMode(RoundingMode.HALF_UP);
        String numStr = decimalFormat.format(max);
        return numStr.split("\\.")[0].length();
    }
    // 将输入数据转换为double数组
    public double[] getInputArray(DMU dmu) {
        double[] inputArray = new double[dmu.getInput().size()];
        int i = 0;
        for (String value : dmu.getInput().values()) {
            inputArray[i] = Double.parseDouble(value);
            i++;
        }
        return inputArray;
    }

    // 将DMU的输出转换为double数组
    public double[] getOutputArray(DMU dmu) {
        double[] outputArray = new double[dmu.getOutput().size()];
        int i = 0;
        for (String value : dmu.getOutput().values()) {
            outputArray[i] = Double.parseDouble(value);
            i++;
        }
        return outputArray;
    }

    // 计算DMU的输出总和
    public double getOutputSum(DMU dmu) {
        double sum = 0;
        for (String value : dmu.getOutput().values()) {
            sum += Double.parseDouble(value);
        }
        return sum;
    }
    // 计算DMU的输出总和
    public double getInputSum(DMU dmu) {
        double sum = 0;
        for (String value : dmu.getInput().values()) {
            sum += Double.parseDouble(value);
        }
        return sum;
    }
    public static double mapToZeroOne(double num, int length) {
        double mappedNum = 0.0;
        int integerLength = 0;
        DecimalFormat decimalFormat = new DecimalFormat("#0.00");
        decimalFormat.setRoundingMode(RoundingMode.HALF_UP);
        String numStr = decimalFormat.format(String.valueOf(num));

        if (numStr.contains(".")) {
            integerLength = numStr.split("\\.")[0].length();
        } else {
            integerLength = numStr.length();
        }
        mappedNum = (double) num/Math.pow(10, -integerLength);

        return  mappedNum;
    }
}
