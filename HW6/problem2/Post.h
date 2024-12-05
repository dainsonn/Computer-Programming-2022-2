#ifndef PROBLEM2_POST_H
#define PROBLEM2_POST_H

#include <ctime>
#include <iostream>
#include <string>
#include <vector>

struct Post {
    int postId;
    std::string userId;
    std::tm rawTime;
    std::string title;
    std::string advertising;
    int likeNumber;
    std::vector<std::string> content;

    Post(std::string userId, int postId, std::tm rawTime,
         std::string title, std::string advertising, int likeNumber, std::vector<std::string> content);

    std::string str() const;

    std::string summary() const;
};

#endif // PROBLEM2_POST_H
