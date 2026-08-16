# CampusEats — System Brief

CampusEats is a food ordering and delivery system which allow students to order food from the resturents around the campus.

Students first log in and manage their profile and delivery addresses. They can then browse available campus restaurants, view their menus and prices, select food items, add them to a cart, and place an order.The system also supports online payment. After an order is placed, a rider is assigned to deliver the food, and the student can track the delivery. The student receives notifications as the order progresses through different stages, including order placed, paid, on the way, and delivered.

The main user mentioned in the CampusEats description is the **student**, who interacts with the system to order and receive food. The system also involves **riders**, who are assigned deliveries and deliver the food. **Campus restaurants** provide the restaurants, menus, and prices that students browse. Behind these user-facing activities, the system is organized around separate capabilities such as accounts, catalogue, orders, payments, delivery, and notifications.

### The main **nouns** in CampusEats are  :

These include **users, addresses, restaurants, menus, prices, carts, orders, order status, transactions, refunds, riders, assignments, and message logs**.

 #### Grouping these nouns into six service areas:

  1. **Accounts** : It owns users, addresses, and login
  2. **Catalogue** : It owns restaurants, menus, and prices;
  3. **Orders** : It owns carts, orders, and status;
  4. **Payments**:  It owns transactions and refunds;
  5. **Delivery** : It owns riders and assignments;
  6. **Notifications** : It owns the message log.

### The main **verbs** are the actions or tasks the system performs :

 These include **manage profile and addresses, browse restaurants and menus, add items to a cart, place an order, pay, refund, assign a rider, track delivery, and send notifications**. And this will be used to identifies these as the six main capabilities used to decide the service boundaries.

##### In short, CampusEats connects students, restaurants, payments, and delivery through a set of clearly separated capabilities. Each capability owns its own data, while other services interact with it through defined contracts rather than directly accessing its internal data. This makes the services independent and loosely coupled.
