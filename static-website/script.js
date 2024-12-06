$(document).ready(function () {
  $("#home").attr("href", "/");

  let allKeys = [];

  get("http://54.93.176.228:8080/s3/images", function (data) {
    data.forEach(element => {
      const imageObj = {
        key: element.key,
        url: element.url
      };
      allKeys.push(imageObj);
    });
    loadMore(allKeys);
    checkIfLoadMoreRequired();
  });

  function get(url, callback) {
    fetch(url, {
      method: "GET",
      headers: {
        Accept: "application/json, text/plain",
      },
    })
      .then((response) => response.json())
      .then((data) => callback(data))
  }

  const MAX_PAGE_SIZE = 6;

  function loadMore(imageKeys) {
    let template = $("#image-key-template").html();
    for (let i = 0; i < MAX_PAGE_SIZE; i++) {
      let imageData = imageKeys.shift();
      if (imageData) {
        $(".row").append(Mustache.render(template, imageData));
      }
    }
    bindAllActions();
  }

  function bindAllActions() {
    $(".delete-button").unbind("click").on("click", deleteImage);

    hideAppearingElements();
    bindForMobileDevices();
  }

  function post(url, contentType, dataToSend, callback) {
    const formData = new FormData();
    formData.append("file", dataToSend)
    const fetchOptions = {
      method: "POST",
      body: formData, // in comparison with Spring server, where we send with FormData - we are sending the file/object directly with serverless implementation
    };
    fetch(url, fetchOptions)
      .then((response) => response.json())
      .then((data) => callback(data))
      .catch((error) => {
        showErrorNotification(error.message);
      });
  }

  function del(url, callback) {
    const fetchOptions = {
      method: "DELETE",
    };
    fetch(url, fetchOptions)
      .then((response) => response.json())
      .then((data) => callback(data))
      .catch((error) => {
        showErrorNotification(error.message);
      });
  }

  function bindExpandedImage() {
    const parent = $(this).closest("img");
    $("#expanded-image").attr("src", parent.attr("src"));
  }

  function hideAppearingElements() {
    $(".description").hide();
    $(".audio").empty().hide();
    $(".hover-buttons").show();
  }

  // Show hover buttons for mobile devices
  function bindForMobileDevices() {
    if ($(window).width() < 992) {
      $(".hover-buttons").css("opacity", "1");
      $(".description, .audio").on("click", hideAppearingElements);
    }
  }

  function checkIfLoadMoreRequired() {
    if (allKeys.length > 0) {
      $("#load-more").show();
    } else {
      $("#load-more").hide();
    }
  }

  // Clear description on click outside or "Info" button click
  $(document).on("mouseover", function (event) {
    if (!$(event.target).closest(".image-card").length) {
      hideAppearingElements();
    }
  });

  $("#upload-file").on("click", function (event) {
    $("#upload-file-input").trigger("click");
    event.preventDefault();
  });

  $("#upload-file-input").on("change", function () {
    const file = $(this).prop("files")[0];
    const url = "http://54.93.176.228:8080/s3/upload";
    post(url, "multipart/form-data", file, function (data) {
      showSuccessUploadNotification(file.name);
      const template = $("#image-key-template").html();
      $(".row").prepend(Mustache.render(template, data));
      bindAllActions();
    });
  });

  function deleteImage() {
    const parent = $(this).closest(".image-card");
    const dataToSend = parent[0] ? parent[0].id : '';
    del("http://54.93.176.228:8080/s3/" + dataToSend, function (data) {
      parent.addClass("delete-animation");
      setTimeout(function () {
        parent.remove();
        console.log(data);
        showSuccessDeleteNotification(data.key);
      }, 500);
    });
  }

  function showSuccessUploadNotification(fileNameText) {
    const templateData = { fileName: fileNameText };
    const alertTemplate = $("#upload-alert-template").html();
    $(".header").prepend(Mustache.render(alertTemplate, templateData));
  }

  function showSuccessDeleteNotification(fileNameText) {
    const templateData = { fileName: fileNameText };
    const alertTemplate = $("#delete-alert-template").html();
    $(".header").prepend(Mustache.render(alertTemplate, templateData));
  }

  function showErrorNotification(errorMessage) {
    const templateData = { message: errorMessage };
    const alertTemplate = $("#error-alert-template").html();
    $(".header").prepend(Mustache.render(alertTemplate, templateData));
  }

  $("#load-more").on("click", function (event) {
    event.preventDefault();
    loadMore(allKeys);
    checkIfLoadMoreRequired();
  });

});