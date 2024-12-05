#ifndef PROBLEM1_ADMINUI_H
#define PROBLEM1_ADMINUI_H

#include <string>
#include <iostream>
#include "UI.h"
#include "ShoppingDB.h"

class AdminUI : public UI {
public:
    AdminUI(ShoppingDB &db, std::ostream& os);
    void AddProduct(std::string name, int price, int count);
    void EditProduct(std::string name, int price, int count);
    void ListProduct();
private:
    std::string product2Str(Product* product, float discountRate) override;
};

#endif //PROBLEM1_ADMINUI_H
