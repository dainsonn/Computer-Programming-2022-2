#include "Console.h"

#include <algorithm>

#include "Post.h"
#include "Utility.h"

Console::Console(std::istream& is, std::ostream& os)
        : is(is), os(os) {}

std::pair<std::string, std::string> Console::readAuthInfoInput() const {
    std::string id, password;
    os << "------ Authentication ------" << std::endl;
    os << "id : ";
    std::getline(is, id);
    os << "passwd : ";
    std::getline(is, password);
    return {id, password};
}

std::vector<std::string> Console::readCommandInput(const std::string& userId) const {
    std::vector<std::string> command = fetchCommandInput(userId);
    while (!isValid(command)) {
        printWrongCommand();
        command = fetchCommandInput(userId);
    }
    return command;
}

static bool isPositive(const std::string& s) {
    if (s.empty()) {
        return false;
    }
    if (std::find_if(s.begin(), s.end(), [](unsigned char c) { return !std::isdigit(c); }) != s.end()) {
        return false;
    } else {
        return std::stoi(s) > 0;
    }
}

bool Console::isValid(const std::vector<std::string>& command) {
    if (command.empty()) {
        return false;
    }
    const auto& commandType = command.front();
    if (commandType == "exit" || commandType == "post") {
        return command.size() == 1;
    } else if (commandType == "recommend") {
        return command.size() == 2 && isPositive(command.back());
    } else if (commandType == "search") {
        return command.size() >= 3 && isPositive(command.back());
    } else {
        return false;
    }
}

std::vector<std::string> Console::fetchCommandInput(const std::string& userId) const {
    std::string rawCommand;
    os << "----------------------------" << std::endl;
    os << userId << "@sns.com" << std::endl;
    os << "post : Post contents" << std::endl;
    os << "recommend <number> : Recommend <number> interesting posts" << std::endl;
    os << "search <keyword1> ... <keywordN> <threshold> : List post entries whose contents contain keywords more than the threshold" << std::endl;
    os << "exit : Terminate this program" << std::endl;
    os << "----------------------------" << std::endl;
    os << "Command : ";
    std::getline(is, rawCommand);
    return split(rawCommand, " ");
}

std::tuple<std::string, std::string, std::vector<std::string>> Console::readPostInput() const {
    std::string title;
    std::string advertising;
    std::vector<std::string> content;
    os << "----------------------------" << std::endl;
    os << "New Post" << std::endl;
    os << "* Title : ";
    std::getline(is, title);
    os << "* Advertising (yes/no) : ";
    std::getline(is, advertising);
    os << "* Content" << std::endl;
    std::string line;
    do {
        os << "> ";
        std::getline(is, line);
        content.push_back(line);
    } while (!line.empty());
    content.erase(content.end() - 1);
    return {title, advertising, content};
}

void Console::printFailedAuth() const {
    os << "----------------------------" << std::endl;
    os << "Failed Authentication." << std::endl;
}

void Console::printWrongCommand() const {
    os << "----------------------------" << std::endl;
    os << "Wrong Command." << std::endl;
}

void Console::printRecommend(const std::vector<Post>& posts) const {
    for (const auto& post: posts) {
        os << "----------------------------" << std::endl;
        os << post.str();
    }
}

void Console::printSearch(const std::vector<Post>& posts) const {
    os << "----------------------------" << std::endl;
    for (const auto& post: posts) {
        os << post.summary() << std::endl;
    }
}
