/*
 * MinMaxParameter.java
 *
 */

package de.unistuttgart.vis.vita.services.responses.parameters;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * Parameter which has additional {@link javax.validation.constraints.Min} and {@link
 * javax.validation.constraints.Max} annotations.
 */
@XmlRootElement
public class MinMaxParameter extends AbstractParameter {

  protected long defaultValue;
  private long min;
  private long max;

  public MinMaxParameter() {
  }

  public MinMaxParameter(String name, String type, long min, long max) {
    super(name, type);
    this.min = min;
    this.max = max;
  }

  @Override
  public Object getDefaultValue() {
    return defaultValue;
  }

  @Override
  public void setDefaultValue(Object defaultValue) {
    this.defaultValue = ((Number)defaultValue).longValue();
  }

  public long getMin() {
    return min;
  }

  public void setMin(long min) {
    this.min = min;
  }

  public long getMax() {
    return max;
  }

  public void setMax(long max) {
    this.max = max;
  }
}
