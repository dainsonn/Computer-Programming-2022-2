#ifndef PROBLEM1_CLIENTUI_H
#define PROBLEM1_CLIENTUI_H

#include <string>
#include <set>
#include "UI.h"
#include "ShoppingDB.h"
#include "User.h"

class ClientUI : public UI
{
public:
    ClientUI(ShoppingDB &db, std::ostream& os);
    void SignUp(std::string userName, std::string password);
    void Login(std::string userName, std::string password);
    void Logout();
    void AddToCart(std::string productName);
    void ListCart();
    void Buy(std::string productName);
    void BuyAllInCart();
    void RecommendProducts();
private:
    User* currentUser;
    std::string product2Str(Product* product, float discountRate) override;
};

#endif //PROBLEM1_CLIENTUI_H
