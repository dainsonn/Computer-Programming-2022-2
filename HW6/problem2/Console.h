#ifndef PROBLEM2_CONSOLE_H
#define PROBLEM2_CONSOLE_H

#include <iostream>
#include <set>
#include <string>
#include <vector>

class Post;

class Console {
public:
    Console(std::istream& is, std::ostream& os);

    std::pair<std::string, std::string> readAuthInfoInput() const;

    std::vector<std::string> readCommandInput(const std::string& userId) const;

    std::tuple<std::string, std::string, std::vector<std::string>> readPostInput() const;

    void printFailedAuth() const;

    void printWrongCommand() const;

    void printRecommend(const std::vector<Post>& posts) const;

    void printSearch(const std::vector<Post>& posts) const;

private:
    static bool isValid(const std::vector<std::string>& command);

    std::vector<std::string> fetchCommandInput(const std::string& userId) const;

    std::istream& is;
    std::ostream& os;
};

#endif // PROBLEM2_CONSOLE_H
