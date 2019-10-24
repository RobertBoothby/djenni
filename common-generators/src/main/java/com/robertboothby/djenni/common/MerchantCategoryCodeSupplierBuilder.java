package com.robertboothby.djenni.common;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.robertboothby.djenni.ConfigurableSupplierBuilder;
import com.robertboothby.djenni.core.StreamableSupplier;

import java.io.InputStream;
import java.util.function.Consumer;

import static com.robertboothby.djenni.core.SupplierHelper.fromValues;

public class MerchantCategoryCodeSupplierBuilder implements ConfigurableSupplierBuilder<MerchantCategoryCode, MerchantCategoryCodeSupplierBuilder> {
    public static final MerchantCategoryCode[] DEFAULT_MCCS;

    static {
        MerchantCategoryCode[] result = null;
        try(InputStream inputStream =  MerchantCategoryCodeSupplierBuilder.class.getResourceAsStream("/mcc_codes.json")) {
            result = new ObjectMapper()
                    .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
                    .readValue(inputStream, MerchantCategoryCode[].class);
        } catch (Exception e) {
            throw e instanceof RuntimeException ? (RuntimeException) e : new RuntimeException(e);
        }
        DEFAULT_MCCS = result;
    }

    private MerchantCategoryCode[] merchantCategoryCodes;

    public MerchantCategoryCodeSupplierBuilder() {
        this.withMerchantCategoryCodes(DEFAULT_MCCS);
    }

    public MerchantCategoryCodeSupplierBuilder withMerchantCategoryCodes(MerchantCategoryCode ... merchantCategoryCodes) {
        this.merchantCategoryCodes = merchantCategoryCodes;
        return this;
    }

    @Override
    public StreamableSupplier<MerchantCategoryCode> build() {
        return fromValues(merchantCategoryCodes);
    }

    public static MerchantCategoryCodeSupplierBuilder merchantCategoryCodeSupplierBuilder() {
        return new MerchantCategoryCodeSupplierBuilder();
    }

    public static StreamableSupplier<MerchantCategoryCode> merchantCategoryCodes(){
        return merchantCategoryCodeSupplierBuilder().build();
    }

    public static StreamableSupplier<MerchantCategoryCode> merchantCategoryCodes(Consumer<MerchantCategoryCodeSupplierBuilder> config){
        return merchantCategoryCodeSupplierBuilder().configure(config).build();
    }
}
