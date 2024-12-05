#ifndef PROBLEM1_USER_H
#define PROBLEM1_USER_H

#include <string>
#include <vector>
#include <algorithm>
#include "Product.h"

class Membership;

class User {
public:
    User(std::string name, std::string password);
    ~User();
    static int lastId;
    float GetDiscountRate() const;
    std::function<bool(User*, User*)> GetUserComparator();

private:
    int id;
    const std::string name;
    const std::string password;
    Membership* membership;

    std::vector<Product*> cart;
    std::vector<Product*> history;
    int totalPayment;
    void updateMembership();
};

#endif //PROBLEM1_USER_H
