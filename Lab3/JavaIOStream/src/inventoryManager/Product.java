package inventoryManager;

public class Product {
	private final String code;
	private final String name;
	private final double unitPrice;
	private final int quantity;

	public Product(String code, String name, double unitPrice, int quantity) {
		if (code == null || code.isBlank()) {
			throw new IllegalArgumentException("Mã không được rỗng");
		}
		if (name == null || name.isBlank()) {
			throw new IllegalArgumentException("Tên không được rỗng");
		}

		if (code.contains(",") || name.contains(",")) {
			throw new IllegalArgumentException("Mã và tên không được chứa dấu phẩy");
		}

		if (!(unitPrice > 0) || !Double.isFinite(unitPrice)) {
			throw new IllegalArgumentException("Đơn giá phải lớn hơn 0 (nhận: " + unitPrice + ")");
		}
		if (quantity < 0) {
			throw new IllegalArgumentException("Số lượng không được âm (nhận: " + quantity + ")");
		}
		this.code = code.trim();
		this.name = name.trim();
		this.unitPrice = unitPrice;
		this.quantity = quantity;
	}

	public String getCode() {
		return code;
	}

	public String getName() {
		return name;
	}

	public double getUnitPrice() {
		return unitPrice;
	}

	public int getQuantity() {
		return quantity;
	}

	public double inventoryValue() {
		return unitPrice * quantity;
	}

	@Override
	public String toString() {
		return String.format("%s - %s: %,.0f x %d = %,.0f VND", code, name, unitPrice, quantity, inventoryValue());
	}
}
