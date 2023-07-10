package io.nwdaf.eventsubscription.client.model;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.validation.annotation.Validated;
import javax.validation.constraints.*;

/**
 * Provides the G-NB identifier.
 */
@Schema(description = "Provides the G-NB identifier.")
@Validated
@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2023-06-10T19:22:40.843464800+03:00[Europe/Athens]")


public class GNbId   {
  @JsonProperty("bitLength")
  private Integer bitLength = null;

  @JsonProperty("gNBValue")
  private String gNBValue = null;

  public GNbId bitLength(Integer bitLength) {
    this.bitLength = bitLength;
    return this;
  }

  /**
   * Unsigned integer representing the bit length of the gNB ID as defined in clause 9.3.1.6 of 3GPP TS 38.413 [11], within the range 22 to 32. 
   * minimum: 22
   * maximum: 32
   * @return bitLength
   **/
  @Schema(required = true, description = "Unsigned integer representing the bit length of the gNB ID as defined in clause 9.3.1.6 of 3GPP TS 38.413 [11], within the range 22 to 32. ")
      @NotNull

  @Min(22) @Max(32)   public Integer getBitLength() {
    return bitLength;
  }

  public void setBitLength(Integer bitLength) {
    this.bitLength = bitLength;
  }

  public GNbId gNBValue(String gNBValue) {
    this.gNBValue = gNBValue;
    return this;
  }

  /**
   * This represents the identifier of the gNB. The value of the gNB ID shall be encoded in hexadecimal representation. Each character in the string shall take a value of \"0\" to \"9\", \"a\" to \"f\" or \"A\" to \"F\" and shall represent 4 bits. The padding 0 shall be added to make multiple nibbles,  the most significant character representing the padding 0 if required together with the 4 most significant bits of the gNB ID shall appear first in the string, and the character representing the 4 least significant bit of the gNB ID shall appear last in the string. 
   * @return gNBValue
   **/
  @Schema(required = true, description = "This represents the identifier of the gNB. The value of the gNB ID shall be encoded in hexadecimal representation. Each character in the string shall take a value of \"0\" to \"9\", \"a\" to \"f\" or \"A\" to \"F\" and shall represent 4 bits. The padding 0 shall be added to make multiple nibbles,  the most significant character representing the padding 0 if required together with the 4 most significant bits of the gNB ID shall appear first in the string, and the character representing the 4 least significant bit of the gNB ID shall appear last in the string. ")
      @NotNull

  @Pattern(regexp="^[A-Fa-f0-9]{6,8}$")   public String getGNBValue() {
    return gNBValue;
  }

  public void setGNBValue(String gNBValue) {
    this.gNBValue = gNBValue;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    GNbId gnbId = (GNbId) o;
    return Objects.equals(this.bitLength, gnbId.bitLength) &&
        Objects.equals(this.gNBValue, gnbId.gNBValue);
  }

  @Override
  public int hashCode() {
    return Objects.hash(bitLength, gNBValue);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class GNbId {\n");
    
    sb.append("    bitLength: ").append(toIndentedString(bitLength)).append("\n");
    sb.append("    gNBValue: ").append(toIndentedString(gNBValue)).append("\n");
    sb.append("}");
    return sb.toString();
  }

  /**
   * Convert the given object to string with each line indented by 4 spaces
   * (except the first line).
   */
  private String toIndentedString(java.lang.Object o) {
    if (o == null) {
      return "null";
    }
    return o.toString().replace("\n", "\n    ");
  }
}
