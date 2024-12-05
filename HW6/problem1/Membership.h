#ifndef PROBLEM1_MEMBERSHIP_H
#define PROBLEM1_MEMBERSHIP_H

#include <functional>
#include <algorithm>

class User;
class Product;

class Membership
{
public:
    virtual ~Membership(){}
    virtual float getDiscountRate() const = 0;
    virtual std::function<bool(User*, User*)> compareUser(User* user) = 0;
    static const int PREMIUM_THRESHOLD = 100000;
};

class Normal : public Membership
{
public:
    float getDiscountRate() const override;
    std::function<bool(User*, User*)> compareUser(User* user) override;
};

class Premium : public Membership
{
public:
    float getDiscountRate() const override;
    std::function<bool(User*, User*)> compareUser(User* user) override;
};


#endif //PROBLEM1_MEMBERSHIP_H
