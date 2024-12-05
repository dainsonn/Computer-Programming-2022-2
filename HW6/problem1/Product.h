#ifndef PROBLEM1_PRODUCT_H
#define PROBLEM1_PRODUCT_H

#include <string>

struct Product {
    Product(std::string name, int price, int count);
    const std::string name;
    int price;
    int count;
};

#endif //PROBLEM1_PRODUCT_H
