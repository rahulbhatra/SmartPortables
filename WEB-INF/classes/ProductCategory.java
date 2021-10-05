public enum ProductCategory {
    WearableTechnology, Phone, Laptop, VoiceAssistant, Accessories;

    public static ProductCategory getEnum(String productCategory) {
        for(ProductCategory productCategoryEnum: ProductCategory.values()) {
            if(productCategoryEnum.toString().equalsIgnoreCase(productCategory)) {
                return productCategoryEnum;
            }
        }
        return null;
    }

}
