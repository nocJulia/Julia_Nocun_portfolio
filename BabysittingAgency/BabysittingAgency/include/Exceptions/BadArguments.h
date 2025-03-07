#ifndef BAYBYSITTINGAGENCY_BADARGUMENTS_H
#define BAYBYSITTINGAGENCY_BADARGUMENTS_H

#include <stdexcept>

class BadArguments : public std::logic_error {
public:
    BadArguments(const std::string &what_arg)
            : std::logic_error(what_arg) {}
};

#endif //BAYBYSITTINGAGENCY_BADARGUMENTS_H








