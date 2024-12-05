#include <cmath>
#include <sstream>
#include "ClientUI.h"
#include "Product.h"

ClientUI::ClientUI(ShoppingDB &db, std::ostream& os) : UI(db, os), currentUser(nullptr) { }

void ClientUI::SignUp(std::string userName, std::string password) {
    // TODO
}

void ClientUI::Login(std::string userName, std::string password) {
    // TODO
}

void ClientUI::Logout() {
    // TODO
}

void ClientUI::Buy(std::string productName) {
    // TODO
}

void ClientUI::AddToCart(std::string productName) {
    // TODO
}

void ClientUI::ListCart() {
    // TODO
}

void ClientUI::BuyAllInCart() {
    // TODO
}

void ClientUI::RecommendProducts() {
    // TODO
}

std::string ClientUI::product2Str(Product* product, float discountRate) {
    std::ostringstream oss;
    oss << "(" << product->name << ", " << std::round((1.0 - discountRate) * product->price) << ")";
    return oss.str();
}

