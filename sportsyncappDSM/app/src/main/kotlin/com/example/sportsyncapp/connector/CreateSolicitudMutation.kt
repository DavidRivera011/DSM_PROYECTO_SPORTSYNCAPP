
@file:Suppress(
  "KotlinRedundantDiagnosticSuppress",
  "PropertyName",
  "MayBeConstant",
  "RedundantVisibilityModifier",
  "RedundantCompanionReference",
  "RemoveEmptyClassBody",
  "SpellCheckingInspection",
  "unused",
)

package com.example.sportsyncapp.connector



public interface CreateSolicitudMutation :
    com.google.firebase.dataconnect.generated.GeneratedMutation<
      DefaultConnector,
      CreateSolicitudMutation.Data,
      CreateSolicitudMutation.Variables
    >
{
  
    @kotlinx.serialization.Serializable
  public data class Variables(
  
    val deporteId: @kotlinx.serialization.Serializable(with = com.google.firebase.dataconnect.serializers.UUIDSerializer::class) java.util.UUID,
  
    val tipoPerfilId: @kotlinx.serialization.Serializable(with = com.google.firebase.dataconnect.serializers.UUIDSerializer::class) java.util.UUID,
  
    val institucionId: com.google.firebase.dataconnect.OptionalVariable<@kotlinx.serialization.Serializable(with = com.google.firebase.dataconnect.serializers.UUIDSerializer::class) java.util.UUID?>,
  
    val equipoId: com.google.firebase.dataconnect.OptionalVariable<@kotlinx.serialization.Serializable(with = com.google.firebase.dataconnect.serializers.UUIDSerializer::class) java.util.UUID?>,
  
    val nombreDeportivo: com.google.firebase.dataconnect.OptionalVariable<String?>,
  
    val fotoDeportiva: com.google.firebase.dataconnect.OptionalVariable<String?>,
  
    val fechaSolicitud: com.google.firebase.dataconnect.LocalDate,
  
  ) {
    
    
      
      @kotlin.DslMarker public annotation class BuilderDsl

      
      @BuilderDsl
      public interface Builder {
        public var deporteId: java.util.UUID
        public var tipoPerfilId: java.util.UUID
        public var institucionId: java.util.UUID?
        public var equipoId: java.util.UUID?
        public var nombreDeportivo: String?
        public var fotoDeportiva: String?
        public var fechaSolicitud: com.google.firebase.dataconnect.LocalDate
        
      }

      public companion object {
        
        @Suppress("NAME_SHADOWING")
        public fun build(
          deporteId: java.util.UUID,tipoPerfilId: java.util.UUID,fechaSolicitud: com.google.firebase.dataconnect.LocalDate,
          block_: Builder.() -> Unit
        ): Variables {
          var deporteId= deporteId
            var tipoPerfilId= tipoPerfilId
            var institucionId: com.google.firebase.dataconnect.OptionalVariable<java.util.UUID?> =
                com.google.firebase.dataconnect.OptionalVariable.Undefined
            var equipoId: com.google.firebase.dataconnect.OptionalVariable<java.util.UUID?> =
                com.google.firebase.dataconnect.OptionalVariable.Undefined
            var nombreDeportivo: com.google.firebase.dataconnect.OptionalVariable<String?> =
                com.google.firebase.dataconnect.OptionalVariable.Undefined
            var fotoDeportiva: com.google.firebase.dataconnect.OptionalVariable<String?> =
                com.google.firebase.dataconnect.OptionalVariable.Undefined
            var fechaSolicitud= fechaSolicitud
            

          return object : Builder {
            override var deporteId: java.util.UUID
              get() = throw UnsupportedOperationException("getting builder values is not supported")
              set(value_) { deporteId = value_ }
              
            override var tipoPerfilId: java.util.UUID
              get() = throw UnsupportedOperationException("getting builder values is not supported")
              set(value_) { tipoPerfilId = value_ }
              
            override var institucionId: java.util.UUID?
              get() = throw UnsupportedOperationException("getting builder values is not supported")
              set(value_) { institucionId = com.google.firebase.dataconnect.OptionalVariable.Value(value_) }
              
            override var equipoId: java.util.UUID?
              get() = throw UnsupportedOperationException("getting builder values is not supported")
              set(value_) { equipoId = com.google.firebase.dataconnect.OptionalVariable.Value(value_) }
              
            override var nombreDeportivo: String?
              get() = throw UnsupportedOperationException("getting builder values is not supported")
              set(value_) { nombreDeportivo = com.google.firebase.dataconnect.OptionalVariable.Value(value_) }
              
            override var fotoDeportiva: String?
              get() = throw UnsupportedOperationException("getting builder values is not supported")
              set(value_) { fotoDeportiva = com.google.firebase.dataconnect.OptionalVariable.Value(value_) }
              
            override var fechaSolicitud: com.google.firebase.dataconnect.LocalDate
              get() = throw UnsupportedOperationException("getting builder values is not supported")
              set(value_) { fechaSolicitud = value_ }
              
            
          }.apply(block_)
          .let {
            Variables(
              deporteId=deporteId,tipoPerfilId=tipoPerfilId,institucionId=institucionId,equipoId=equipoId,nombreDeportivo=nombreDeportivo,fotoDeportiva=fotoDeportiva,fechaSolicitud=fechaSolicitud,
            )
          }
        }
      }
    
  }
  

  
    @kotlinx.serialization.Serializable
  public data class Data(
  
    val solicitudes_insert: SolicitudesKey,
  
  ) {
    
    
  }
  

  public companion object {
    public val operationName: String = "CreateSolicitud"

    public val dataDeserializer: kotlinx.serialization.DeserializationStrategy<Data> =
      kotlinx.serialization.serializer()

    public val variablesSerializer: kotlinx.serialization.SerializationStrategy<Variables> =
      kotlinx.serialization.serializer()
  }
}

public fun CreateSolicitudMutation.ref(
  
    deporteId: java.util.UUID,tipoPerfilId: java.util.UUID,fechaSolicitud: com.google.firebase.dataconnect.LocalDate,

  
    block_: CreateSolicitudMutation.Variables.Builder.() -> Unit = {}
  
): com.google.firebase.dataconnect.MutationRef<
    CreateSolicitudMutation.Data,
    CreateSolicitudMutation.Variables
  > =
  ref(
    
      CreateSolicitudMutation.Variables.build(
        deporteId=deporteId,tipoPerfilId=tipoPerfilId,fechaSolicitud=fechaSolicitud,
  
    block_
      )
    
  )

public suspend fun CreateSolicitudMutation.execute(

  
    
      deporteId: java.util.UUID,tipoPerfilId: java.util.UUID,fechaSolicitud: com.google.firebase.dataconnect.LocalDate,

  
    block_: CreateSolicitudMutation.Variables.Builder.() -> Unit = {}

  ): com.google.firebase.dataconnect.MutationResult<
    CreateSolicitudMutation.Data,
    CreateSolicitudMutation.Variables
  > =
  ref(
    
      deporteId=deporteId,tipoPerfilId=tipoPerfilId,fechaSolicitud=fechaSolicitud,
  
    block_
    
  ).execute()


