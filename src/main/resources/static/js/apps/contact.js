$(function () {
  
    $("#btn-add-contact").on("click", function (event) {
      $("#addContactModal #btn-add").show();
      $("#addContactModal #btn-edit").hide();
      $("#addContactModal").modal("show");
    });
  
    function deleteContact() {
      $(".delete").on("click", function (event) {
        event.preventDefault();
        /* Act on the event */
        $(this).parents(".search-items").remove();
      });
    }

    $("#addContactModal").on("hidden.bs.modal", function (e) {
      var $_name = document.getElementById("c-name");
      var $_email = document.getElementById("c-email");
      var $_occupation = document.getElementById("c-occupation");
      var $_phone = document.getElementById("c-phone");
      var $_location = document.getElementById("c-location");
      var $_getValidationField =
        document.getElementsByClassName("validation-text");
  
      var $_setNameValueEmpty = ($_name.value = "");
      var $_setEmailValueEmpty = ($_email.value = "");
      var $_setOccupationValueEmpty = ($_occupation.value = "");
      var $_setPhoneValueEmpty = ($_phone.value = "");
      var $_setLocationValueEmpty = ($_location.value = "");
  
      for (var i = 0; i < $_getValidationField.length; i++) {
        e.preventDefault();
        $_getValidationField[i].style.display = "none";
      }
    });

    function editContact() {
      $(".edit").on("click", function (event) {
        $("#editContactModal #btn-edit").show();
        $("#editContactModal").modal("show");

        // Get Parents
        var getParentItem = $(this).parents(".search-items");
        var getModal = $("#editContactModal");

        // Get List Item Fields
        var $_name = getParentItem.find(".user-name");
        var $_email = getParentItem.find(".usr-email-addr");
        var $_occupation = getParentItem.find(".user-work");
        var $_phone = getParentItem.find(".usr-ph-no");
        var $_location = getParentItem.find(".usr-location");

        // Get Attributes
        var $_nameAttrValue = $_name.attr("data-name");
        var $_emailAttrValue = $_email.attr("data-email");
        var $_occupationAttrValue = $_occupation.attr("data-occupation");
        var $_phoneAttrValue = $_phone.attr("data-phone");
        var $_locationAttrValue = $_location.attr("data-location");

        // Get Modal Attributes
        var $_getModalNameInput = getModal.find("#c-name");
        var $_getModalEmailInput = getModal.find("#c-email");
        var $_getModalOccupationInput = getModal.find("#c-occupation");
        var $_getModalPhoneInput = getModal.find("#c-phone");
        var $_getModalLocationInput = getModal.find("#c-location");

        // Set Modal Field's Value
        var $_setModalNameValue = $_getModalNameInput.val($_nameAttrValue);
        var $_setModalEmailValue = $_getModalEmailInput.val($_emailAttrValue);
        var $_setModalOccupationValue = $_getModalOccupationInput.val(
          $_occupationAttrValue
        );
        var $_setModalPhoneValue = $_getModalPhoneInput.val($_phoneAttrValue);
        var $_setModalLocationValue =
          $_getModalLocationInput.val($_locationAttrValue);

        $("#addContactModal").modal("show");

        $("#btn-edit").click(function () {
          var getParent = $(this).parents(".modal-content");

          var $_getInputName = getParent.find("#c-name");
          var $_getInputNmail = getParent.find("#c-email");
          var $_getInputNccupation = getParent.find("#c-occupation");
          var $_getInputNhone = getParent.find("#c-phone");
          var $_getInputNocation = getParent.find("#c-location");

          var $_nameValue = $_getInputName.val();
          var $_emailValue = $_getInputNmail.val();
          var $_occupationValue = $_getInputNccupation.val();
          var $_phoneValue = $_getInputNhone.val();
          var $_locationValue = $_getInputNocation.val();

          var setUpdatedNameValue = $_name.text($_nameValue);
          var setUpdatedEmailValue = $_email.text($_emailValue);
          var setUpdatedOccupationValue = $_occupation.text($_occupationValue);
          var setUpdatedPhoneValue = $_phone.text($_phoneValue);
          var setUpdatedLocationValue = $_location.text($_locationValue);

          var setUpdatedAttrNameValue = $_name.attr("data-name", $_nameValue);
          var setUpdatedAttrEmailValue = $_email.attr("data-email", $_emailValue);
          var setUpdatedAttrOccupationValue = $_occupation.attr(
            "data-occupation",
            $_occupationValue
          );
          var setUpdatedAttrPhoneValue = $_phone.attr("data-phone", $_phoneValue);
          var setUpdatedAttrLocationValue = $_location.attr(
            "data-location",
            $_locationValue
          );
          $("#addContactModal").modal("hide");
        });
      });
    }

    $(".delete-multiple").on("click", function () {
      var inboxCheckboxParents = $(".contact-chkbox:checked").parents(
        ".search-items"
      );
      inboxCheckboxParents.remove();
    });

    deleteContact();
    addContact();
    editContact();
  });
  
  // Validation Process
  
  var $_getValidationField = document.getElementsByClassName("validation-text");
  var reg = /^.+@[^\.].*\.[a-z]{2,}$/;
  var phoneReg = /^\d{11}$/;
  
  getNameInput = document.getElementById("c-name");
  
  getNameInput.addEventListener("input", function () {
    getNameInputValue = this.value;
  
    if (getNameInputValue == "") {
      $_getValidationField[0].innerHTML = "Name Required";
      $_getValidationField[0].style.display = "block";
    } else {
      $_getValidationField[0].style.display = "none";
    }
  });
  
  getEmailInput = document.getElementById("c-email");
  
  getEmailInput.addEventListener("input", function () {
    getEmailInputValue = this.value;
  
    if (getEmailInputValue == "") {
      $_getValidationField[1].innerHTML = "Email Required";
      $_getValidationField[1].style.display = "block";
    } else if (reg.test(getEmailInputValue) == false) {
      $_getValidationField[1].innerHTML = "Invalid Email";
      $_getValidationField[1].style.display = "block";
    } else {
      $_getValidationField[1].style.display = "none";
    }
  });
  
  getPhoneInput = document.getElementById("c-phone");
  
  getPhoneInput.addEventListener("input", function () {
    getPhoneInputValue = this.value;
  
    if (getPhoneInputValue == "") {
      $_getValidationField[2].innerHTML = "Phone Number Required";
      $_getValidationField[2].style.display = "block";
    } else if (phoneReg.test(getPhoneInputValue) == false) {
      $_getValidationField[2].innerHTML = "Invalid (Enter 11 Digits)";
      $_getValidationField[2].style.display = "block";
    } else {
      $_getValidationField[2].style.display = "none";
    }
  });