package JavaBean;

//和 商品表 中的字段相对应
public class Goods {
    private String img;//商品图片---链接
    private String id;//商品id
    private String describe;//描述
    private String price;//价格
    private String address;//发货地址
    private int number;//推荐人数
    private String type;//类别
    private int buy;//购买人数

    public void setImg(String img) {
        this.img = img;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setDescribe(String describe) {
        this.describe = describe;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getImg() {
        return img;
    }

    public String getId() {
        return id;
    }

    public String getDescribe() {
        return describe;
    }

    public String getPrice() {
        return price;
    }

    public String getAddress() {
        return address;
    }

    public int getNumber() {
        return number;
    }

    public String getType() {
        return type;
    }

    public int getBuy() {
        return buy;
    }

    public void setBuy(int buy) {
        this.buy = buy;
    }



}

