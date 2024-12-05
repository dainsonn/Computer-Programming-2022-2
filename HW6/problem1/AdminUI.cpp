#include <cmath>
#include <sstream>
#include "AdminUI.h"
#include "Product.h"

AdminUI::AdminUI(ShoppingDB &db, std::ostream& os): UI(db, os) { }

void AdminUI::AddProduct(std::string name, int price, int count) {
    // TODO
}

void AdminUI::EditProduct(std::string name, int price, int count) {
    // TODO
}

void AdminUI::ListProduct() {
    // TODO
}

std::string AdminUI::product2Str(Product* product, float discountRate) {
    std::ostringstream oss;
    oss << "(" << product->name << ", " << std::round((1.0 - discountRate) * product->price) << ", " << product->count << ")";
    return oss.str();
}

