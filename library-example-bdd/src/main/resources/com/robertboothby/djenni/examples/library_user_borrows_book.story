Scenario: A library user borrows a book.

Given a library exists containing some books
And the library has some users
When a library user borrows a book
Then noone else can borrow it