#ifndef PROBLEM1_UI_H
#define PROBLEM1_UI_H

#include <sstream>
#include <iostream>
#include "ShoppingDB.h"

class Product;

class UI {
public:
    UI(ShoppingDB &db, std::ostream& os);
    std::ostream& getOs() const;
protected:
    std::ostream& os;
    ShoppingDB& db;
    virtual std::string product2Str(Product* product, float discountRate) = 0;
};

#endif //PROBLEM1_UI_H
