package com.example.ecommerce_app.feature_shopping.domain.use_case.address

import javax.inject.Inject

class AddressOperationsUseCase @Inject constructor(
    val addAddressUseCase: AddAddressUseCase,
    val updateAddressUseCase: UpdateAddressUseCase,
    val deleteAddressUseCase: DeleteAddressUseCase
)