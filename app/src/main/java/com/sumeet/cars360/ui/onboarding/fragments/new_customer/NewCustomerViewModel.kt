package com.sumeet.cars360.ui.onboarding.fragments.new_customer

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sumeet.cars360.data.remote.model.car_entities.CarEntitiesResponse
import com.sumeet.cars360.data.repository.RemoteRepository
import com.sumeet.cars360.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject

@HiltViewModel
class NewCustomerViewModel @Inject constructor(
    private val remoteRepository: RemoteRepository
) : ViewModel() {

    private val _insertOperation = MutableLiveData<Resource<String>>()
    val insertOperation: LiveData<Resource<String>>
        get() = _insertOperation

    var name: String = ""
    var email: String = ""
    var mobileNo: String = ""
    var firebaseId: String = ""
    var address: String = ""
    var city: String = ""
    var state: String = ""
    var pinCode: String = ""
    var dob: String = ""
    var dom: String = ""
    var gstIn: String = ""
    var profileImage: File? = null

    fun insertNewUserData() {
        _insertOperation.postValue(Resource.Loading())

        viewModelScope.launch(Dispatchers.IO) {
            val response = remoteRepository.addNewUserToServer(
                name,
                email,
                mobileNo,
                firebaseId,
                address,
                city,
                state,
                pinCode,
                dob,
                dom,
                gstIn,
                profileImage
            )

            if (response.isSuccessful && response.body() != null) {
                //TODO revert temp changes
                if (response.body()?.error == true)
                    _insertOperation.postValue(Resource.Success(""))
                //_userInsertOperation.postValue(Resource.Error(response.body()?.userInsertResponse?.get(0)?.message))
                else {
                    val userId = response.body()?.userInsertResponse?.get(0)?.userId
                    _insertOperation.postValue(Resource.Success(userId))
                }
            } else
                _insertOperation.postValue(Resource.Error(response.message()))
        }
    }

    private val _carEntities = MutableLiveData<Resource<CarEntitiesResponse>>()
    val carEntities: LiveData<Resource<CarEntitiesResponse>>
        get() = _carEntities

    fun getAllCarEntities() {
        _carEntities.postValue(Resource.Loading())
        viewModelScope.launch {
            val response = remoteRepository.getAllCarCollections()
            if (response.isSuccessful && response.body() != null)
                if (response.body()?.error == false)
                    _carEntities.postValue(Resource.Success(response.body()))
                else
                    _carEntities.postValue(Resource.Error(response.body()?.message))
            else
                _carEntities.postValue(Resource.Error(response.message()))
        }
    }

    var modelId: String = ""
    var brandId: String = ""
    var vehicleNo: String = ""
    var bodyColor: String = ""
    var fuelType: String = ""
    var insuranceCompany: String = ""
    var insuranceExpiryDate: String = ""


    fun insertNewCar(userId: String) {
        _insertOperation.postValue(Resource.Loading())

        viewModelScope.launch(Dispatchers.IO) {
            val response = remoteRepository.addNewCarDetails(
                userId,
                modelId,
                brandId,
                vehicleNo,
                bodyColor,
                vehicleNo,
                fuelType,
                insuranceCompany,
                insuranceExpiryDate,
                userId
            )

            if (response.isSuccessful && response.body() != null) {
                //TODO revert temp changes
                if (response.body()?.error == true)
                    _insertOperation.postValue(Resource.Success(""))
                //_userInsertOperation.postValue(Resource.Error(response.body()?.userInsertResponse?.get(0)?.message))
                else {
                    val carId = response.body()?.carInsertResponse?.get(0)?.carId
                    _insertOperation.postValue(Resource.Success(carId))
                }
            } else
                _insertOperation.postValue(Resource.Error(response.message()))
        }
    }

}