import java.util.Arrays;

public enum ProductManufacturers {
    FitnessWatches, SmartWatches, HeadPhones, VirtualReality, PetTracker,
    Apple, Samsung, Motorola, Google, OnePlus, MicroSoft, Dell, HP, Lenovo, Navi, Rufus,
    Amazon, Sony, Nintendo;

    public static ProductManufacturers getEnum(String productManufacturer) {
        for (ProductManufacturers productManufacturerEnum : ProductManufacturers.values()) {
            if (productManufacturerEnum.toString().equalsIgnoreCase(productManufacturer)) {
                System.out.println("------------- productManufacturer Want: " + productManufacturer + " Got: " +
                        productManufacturerEnum.toString());
                return productManufacturerEnum;
            }
        }
        return null;
    }

}
