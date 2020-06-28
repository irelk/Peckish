package foodorderingapp.com.foodorderingapp.Model;

public class Product {

        private String type;
        private String name;
        private String price;
        private String info;
        private String imageId;
        private String size;
        private String key;

        public Product()
        {

        }

        public Product(String type, String name,String price,String info, String imageId) {

            this.type = type;
            this.name = name;
            this.price = price;
            this.size = "100";
            this.info = info;
            this.imageId = imageId;
        }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getPrice() {
            return price;
        }

        public void setPrice(String price) {
            this.price = price;
        }

        public String getInfo() {
            return info;
        }

        public void setInfo(String info) {
            this.info = info;
        }

        public String getImageId() {
            return imageId;
        }

        public void setImageId(String imageId) {
            this.imageId = imageId;
        }

}
