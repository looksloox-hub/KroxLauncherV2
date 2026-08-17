package com.example.client.setting;

public class NumberSetting extends Setting {
   private double value;
   private final double min;
   private final double max;
   private final double step;

   public NumberSetting(String name, double value, double min, double max, double step) {
      super(name);
      this.min = min;
      this.max = max;
      this.step = step;
      this.setValue(value);
   }

   public double getValue() {
      return this.value;
   }

   public void setValue(double value) {
      double clamped = Math.max(this.min, Math.min(this.max, value));
      this.value = (double)Math.round(clamped / this.step) * this.step;
   }

   public void increase() {
      this.setValue(this.value + this.step);
   }

   public void decrease() {
      this.setValue(this.value - this.step);
   }

   public double getMin() {
      return this.min;
   }

   public double getMax() {
      return this.max;
   }

   public double getIncrement() {
      return this.step;
   }

   public double getStep() {
      return this.step;
   }
}
