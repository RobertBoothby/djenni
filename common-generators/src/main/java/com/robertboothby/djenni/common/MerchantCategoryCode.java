package com.robertboothby.djenni.common;

import com.fasterxml.jackson.annotation.JsonProperty;

public class MerchantCategoryCode {

    private final int mccCode;
    private final String editedDescription;
    private final String combinedDescription;
    private final String usdaDescription;
    private final String irs_description;
    private final String irsReportabble;

    /*
      {
    "mcc": "0742",
    "edited_description": "Veterinary Services",
    "combined_description": "Veterinary Services",
    "usda_description": "Veterinary Services",
    "irs_description": "Veterinary Services",
    "irs_reportable": "Yes",
    "id": 0
  }
     */
    public MerchantCategoryCode(
            @JsonProperty("mcc") int mccCode,
            @JsonProperty("edited_description") String editedDescription,
            @JsonProperty("combined_description") String combinedDescription,
            @JsonProperty("usda_description") String usdaDescription,
            @JsonProperty("irs_description") String irs_description,
            @JsonProperty("irs_reportable") String irsReportabble) {
        this.mccCode = mccCode;
        this.editedDescription = editedDescription;
        this.combinedDescription = combinedDescription;
        this.usdaDescription = usdaDescription;
        this.irs_description = irs_description;
        this.irsReportabble = irsReportabble;
    }

    public int getMccCode() {
        return mccCode;
    }

    public String getEditedDescription() {
        return editedDescription;
    }

    public String getCombinedDescription() {
        return combinedDescription;
    }

    public String getUsdaDescription() {
        return usdaDescription;
    }

    public String getIrs_description() {
        return irs_description;
    }

    public String getIrsReportabble() {
        return irsReportabble;
    }
}
