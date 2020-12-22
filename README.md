# **Sofra**

**Sofra** is an application With a double interface, Customer or Restaurant.
* As a restaurant offer food for sale and add offers.
* And as a customer you can buy food from any restaurant available.

## Sofra API
**Sofra** uses the [ipda3 API](http://www.ipda3.com/) to sending and receiving data to and from server.
To use the API, you will need to be trainee at [ipda3-tech](https://www.facebook.com/ipda3tech/), Click on the link to contact with ipda3 facebook page.

## Features
* Login
* Register
* Reset password
* Change password
* Profile view and edit
* **Restaurant** can.
  * Add, edit and delete food category
  * Add, edit and delete food meal
  * Add, edit and delete food offers
  * Accept and cancel order
  * Call customer number
  * Receive notifications
  * See total payment and app commission

## Libraries Used
 * [Foundation][0] - Components for core system capabilities, Java extensions and support for
   multidex and automated testing.
   * [AppCompat][1] - Degrade gracefully on older versions of Android.
 * [Architecture][2] - A collection of libraries that help you design robust, testable, and
   maintainable apps. Start with classes for managing your UI component lifecycle and handling data
   persistence.
   * [View Binding][3] - Declaratively bind observable data to UI elements.
   * [Lifecycles][4] - Create a UI that automatically responds to lifecycle events.
   * [ViewModel][5] - Store UI-related data that isn't destroyed on app rotations. Easily schedule
      asynchronous tasks for optimal execution.
 * [UI][6] - Details on why and how to use UI Components in your apps - together or separate
   * [Fragment][7] - A basic unit of composable UI.
   * [Layout][8] - Lay out widgets using different algorithms.
 * Third party and miscellaneous libraries
   * [Glide][9] for image loading
   * [Retrofit ][10] A type-safe HTTP client for Android and Java.
   * [SDP][11] and [SSP][12] An android lib that provides a new size unit. This size unit scales with the screen size. It can help Android developers with supporting multiple screens.
   * [SwipeRevealLayout][13] Easy, flexible and powerful Swipe Layout for Android.
   * [prettytime][14] Social Style Date and Time Formatting for Java.

 [0]: https://developer.android.com/jetpack/components
 [1]: https://developer.android.com/topic/libraries/support-library/packages#v7-appcompat
 [2]: https://developer.android.com/jetpack/arch/
 [3]: https://developer.android.com/topic/libraries/view-binding
 [4]: https://developer.android.com/topic/libraries/architecture/lifecycle
 [5]: https://developer.android.com/topic/libraries/architecture/viewmodel
 [6]: https://developer.android.com/guide/topics/ui
 [7]: https://developer.android.com/guide/components/fragments
 [8]: https://developer.android.com/guide/topics/ui/declaring-layout
 [9]: https://bumptech.github.io/glide/
 [10]: https://square.github.io/retrofit/
 [11]: https://github.com/intuit/sdp
 [12]: https://github.com/intuit/ssp
 [13]: https://github.com/chthai64/SwipeRevealLayout
 [14]: https://github.com/ocpsoft/prettytime

## Contributing Bug reports
We use GitHub for bug tracking. Please search the existing issues for your bug and create a new one if the issue is not yet tracked!
[issues](https://github.com/MohamedOmran72/Sofra/issues)

## Application work flow
<img src="appImages/sofra-work-flow.jpg"/>

## License & Copyright
Copyright (c) 2020 **Mohamed Omran**

 [MIT License](LICENSE)
